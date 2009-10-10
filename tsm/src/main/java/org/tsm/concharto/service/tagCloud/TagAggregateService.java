/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.service.tagCloud;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tsm.concharto.dao.UserTagDao;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.service.EventSearchServiceHib;
import org.tsm.concharto.util.TimeRangeFormat;

import com.ibm.icu.util.Calendar;

/**
 * Service to cache and list aggregate data about user tags.  This is useful for
 * displaying a "tag cloud"
 */
public class TagAggregateService {

	private static final String DATE_SEPARATOR = " - ";
	private static final String CURRENT_DECADE = "2000 - ";
	private static final String FIRST_DATE_RAGE = "-1000000 BC - 1 BC";
	private Log log = LogFactory.getLog(TagAggregateService.class);
    private int defaultDaysBack = 20;
    private double maxFont;
    private double minFont;
    
    UserTagDao userTagDao;
    Map<String, CatalogTagCloud> catalogTagClouds = new HashMap<String, CatalogTagCloud>();
	private List<String> catalogList; 
    
    public void setUserTagDao(UserTagDao userTagDao) {
        this.userTagDao = userTagDao;
    }

    public void setDefaultDaysBack(int defaultDaysBack) {
        this.defaultDaysBack = defaultDaysBack;
    }

    public int getDefaultDaysBack() {
        return defaultDaysBack;
    }

    public void setMaxFont(double maxFont) {
        this.maxFont = maxFont;
    }

    public void setMinFont(double minFont) {
        this.minFont = minFont;
    }

    /**
     * Get a list for displaying a tag cloud
     * @param daysBack number of days to go back in time for displaying this info
     * @param catalog catalog to use
     * @return a map of tags showing font size, keyed by tag name.  If nothing is found for
     * the given catalog, an empty list is returned. 
     */
    public List<TagCloudEntry> getRecent(String catalog) {
    	CatalogTagCloud catalogTagCloud = catalogTagClouds.get(catalog);
    	if (null != catalogTagCloud) {
    		return  catalogTagCloud.getRecent();
    	} else { 
    		return new ArrayList<TagCloudEntry>();
    	}
    }
    
    /**
     * Get a map of tag clouds for all time by century, descending order
     * @param catalog catalog to use
     * @return a set of tag clouds sorted in descending order by century.  If nothing is found for
     * the given catalog, an empty list is returned.  
     */
    public SortedMap<TimeRange, List<TagCloudEntry>> getTagIndex(String catalog) {
    	CatalogTagCloud catalogTagCloud = catalogTagClouds.get(catalog);
    	if (null != catalogTagCloud) {
    		return  catalogTagCloud.getTagIndex();
    	} else { 
    		return new TreeMap<TimeRange, List<TagCloudEntry>>();
    	}
    }

    /**
     * Refresh all indexes.  Normally called from a timer task (e.g. quartz)
     */
    public void refresh() {
    	catalogList = refreshCatalogList();
        refreshRecent(catalogList);
        refreshIndex(catalogList);
    }

    /**
     * Refresh the list of all catalogs (e.g. www, dev, boating)
     */
    private List<String> refreshCatalogList() {
    	return userTagDao.getDistinctCatalogs();
	}

	/**
     * Refresh the recent tagCounts
     */
    private void refreshRecent(List<String> catalogList) {
    	for (String catalog : catalogList) {
            refreshRecent(catalog);
    	}
    }

	public void refreshRecent(String catalog) {
		List<TagCloudEntry> tagCounts = userTagDao.getTagCounts(defaultDaysBack);
		List<TagCloudEntry> recentCloud = makeTagCloud(catalog, tagCounts);
		addToCatalogTagCloud(catalog, recentCloud); 
		log.info("Catalog " + catalog + ", loaded " + recentCloud.size() + " tags");
	}

    /**
     * Make a count of tags
     * @param tagCloudEntries
     * @return
     */
    private List<TagCloudEntry> makeTagCloud(String catalog, List<TagCloudEntry> tagCloudEntries) {
        List<TagCloudEntry> cloud = new ArrayList<TagCloudEntry>();
        //first tabulate the min and max number of occurrences of any tag in the collection
        long max = Integer.MIN_VALUE;
        long min = Integer.MAX_VALUE;
        for (TagCloudEntry tagCloudEntry : tagCloudEntries) {
            if (tagCloudEntry.catalog.equals(catalog)) {
                Long count = (tagCloudEntry.getCount()).longValue();
                if (count > max) {
                    max = count;
                }
                if (count < min) {
                    min = count;
                }
            }
        }
        
        int untaggedCount = 0;
        for (TagCloudEntry tagCloudEntry : tagCloudEntries) {
            if (tagCloudEntry.catalog.equals(catalog)) {
                Long count = (tagCloudEntry.getCount()).longValue();
                String tag = tagCloudEntry.getTag();
                //we don't automatically add empty tags because we want to merge nulls together
                //with empty (both null and "" exists in the db) 
                if (StringUtils.isEmpty(tag)) {
                    untaggedCount += count;
                } else {
                    cloud.add(new TagCloudEntry(tag, getFontSize(count, min, max)));
                }
            }
        }
        if (untaggedCount != 0) {
            cloud.add(new TagCloudEntry(
                    EventSearchServiceHib.UNTAGGED, getFontSize(untaggedCount, min, max)));
        }
        return cloud;
    }
    
    /**
     * Add a recent cloud to the catalog holder
     * @param catalog
     * @param recent
     */
    private void addToCatalogTagCloud(String catalog, List<TagCloudEntry> recent) {
    	CatalogTagCloud catalogTagCloud = catalogTagClouds.get(catalog);
    	if (null == catalogTagCloud) {
    		catalogTagCloud = new CatalogTagCloud();   		
    	}
    	catalogTagCloud.setRecent(recent);
    	catalogTagClouds.put(catalog, catalogTagCloud);
    }

    /**
     * Add an index to the catalog holder
     * @param catalog
     * @param tagIndex
     */
    private void addToCatalogTagCloud(String catalog, SortedMap<TimeRange, List<TagCloudEntry>> tagIndex) {
    	CatalogTagCloud catalogTagCloud = catalogTagClouds.get(catalog);
    	if (null == catalogTagCloud) {
    		catalogTagCloud = new CatalogTagCloud();   		
    	}
    	catalogTagCloud.setTagIndex(tagIndex);
    	catalogTagClouds.put(catalog, catalogTagCloud);
    }

    /**
     * Calculate the tag cloud font size given the value and the min/max
     * @param object
     * @param min
     * @param max
     * @return
     */
    private Integer getFontSize(long value, long min, long max) {
        Double fontSize;
        if (max == min) {
            fontSize = new Double(maxFont);
        } else {
            fontSize = minFont + ((maxFont - minFont)/ (new Double(max)-min)) * (new Double(value - min));
        }
        return new Long(Math.round(fontSize)).intValue();
    }
    
    /**
     * Refresh the index of all tags, arranged by century
     */
    private void refreshIndex(List<String> catalogList) {
    	for (String catalog : catalogList) {
            //All BC
        	SortedMap<TimeRange, List<TagCloudEntry>> 
            	tagIndex = new TreeMap<TimeRange, List<TagCloudEntry>>(new Comparator<TimeRange>() {
                public int compare(TimeRange arg0, TimeRange arg1) {
                    //sorted descending
                    return arg1.getBegin().getDate().compareTo(arg0.getBegin().getDate());
                }});
            try {
                Calendar cal = Calendar.getInstance();
                addToIndex(catalog, tagIndex, CURRENT_DECADE + cal.get(Calendar.YEAR));
                int start = 0;
                for (int end = 1999; end>=99; end-=100) {
                    start = end - 99;
                    if (start == 0) {
                        start = 1;  //0 AD is not a valid date
                    }
                    addToIndex(catalog, tagIndex, start + DATE_SEPARATOR + end);
                }
                addToIndex(catalog, tagIndex, FIRST_DATE_RAGE);
                
            } catch (ParseException e) {
                log.error("couldn't calculate index", e);
            }
            addToCatalogTagCloud(catalog, tagIndex); 
    	}
    }

    /**
     * Add a tag cloud to the index
     * @param timeRange string representation of the time range
     * @throws ParseException 
     */
    private void addToIndex(String catalog, SortedMap<TimeRange, List<TagCloudEntry>> tagIndex, String timeRange) throws ParseException {
        TimeRange tr = TimeRangeFormat.parse(timeRange);
        List<TagCloudEntry> tagCloudEntries = userTagDao.getTagCountsByEventBeginDate(tr);
        List<TagCloudEntry> tagCloud = makeTagCloud(catalog, tagCloudEntries);
        tagIndex.put(tr, tagCloud);
        log.info("Catalog " + catalog + ", added to index " + tagCloud.size() + " tags for time range " + timeRange);
    }

}
