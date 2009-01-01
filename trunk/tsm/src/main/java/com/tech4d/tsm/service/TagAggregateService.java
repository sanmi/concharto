package com.tech4d.tsm.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.util.Calendar;
import com.tech4d.tsm.dao.UserTagDao;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.util.TimeRangeFormat;

/**
 * Service to cache and list aggregate data about user tags.  This is useful for
 * displaying a "tag cloud"
 */
public class TagAggregateService {

    private Log log = LogFactory.getLog(TagAggregateService.class);
    private int defaultDaysBack = 20;
    private double maxFont;
    private double minFont;
    
    UserTagDao userTagDao;
    List<TagCloudEntry> tagCloud;
    SortedMap<TimeRange, List<TagCloudEntry>> tagIndex;
    
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
     * @return a map of tags showing font size, keyed by tag name
     */
    public List<TagCloudEntry> getTagCloud() {
        return tagCloud;
    }
    
    /**
     * Get a map of tag clouds for all time by century, descending order
     * @return
     */
    public SortedMap<TimeRange, List<TagCloudEntry>> getTagIndex() {
        return tagIndex;
    }

    /**
     * Refresh all indexes.  Normally called from a timer task (e.g. quartz)
     */
    public void refresh() {
        refreshRecent();
        refreshIndex();
    }
    
    /**
     * Refresh the recent tagCounts
     */
    public void refreshRecent() {
        List<Object[]> tagCounts = userTagDao.getTagCounts(defaultDaysBack);
        tagCloud = makeTagCloud(tagCounts);
        log.info("loaded " + tagCloud.size() + " tags");
    }

    /**
     * Make a count of tags
     * @param tagCounts
     * @return
     */
    private List<TagCloudEntry> makeTagCloud(List<Object[]> tagCounts) {
        List<TagCloudEntry> cloud = new ArrayList<TagCloudEntry>();
        long max = Integer.MIN_VALUE;
        long min = Integer.MAX_VALUE;
        for (Object[] tagCount : tagCounts) {
            Long count = ((BigInteger)tagCount[1]).longValue();
            if (count > max) {
                max = count;
            }
            if (count < min) {
                min = count;
            }
        }
        for (Object[] tagCount : tagCounts) {
            Long count = ((BigInteger)tagCount[1]).longValue();
            String tag = (String)tagCount[0];
            if (StringUtils.isEmpty(tag)) {
                tag = EventSearchServiceHib.UNTAGGED;
            }
            cloud.add(new TagCloudEntry(tag, getFontSize(count, min, max)));
        }
        return cloud;
    }

    /**
     * Calculate the tag cloud font size given the value and the min/max
     * @param object
     * @param min
     * @param max
     * @return
     */
    private Integer getFontSize(long value, long min, long max) {
        Double fontSize = minFont + ((maxFont - minFont)/ (new Double(max)-1)) * (new Double(value - min));
        return new Long(Math.round(fontSize)).intValue();
    }
    
    /**
     * Refresh the index of all tags, arranged by century
     */
    public void refreshIndex() {
        //All BC
        tagIndex = new TreeMap<TimeRange, List<TagCloudEntry>>(new Comparator<TimeRange>() {
            public int compare(TimeRange arg0, TimeRange arg1) {
                //sorted descending
                return arg1.getBegin().getDate().compareTo(arg0.getBegin().getDate());
            }});
        try {
            Calendar cal = Calendar.getInstance();
            addToIndex("2000 - " + cal.get(Calendar.YEAR));
            int start = 0;
            for (int end = 1999; end>=99; end-=100) {
                start = end - 99;
                if (start == 0) {
                    start = 1;  //0 AD is not a valid date
                }
                addToIndex(start + " - " + end);
            }
            addToIndex("-1000000 BC - 1 BC");
            
        } catch (ParseException e) {
            log.error("couldn't calculate index", e);
        }
    }

    /**
     * Add a tag cloud to the index
     * @param timeRange string representation of the time range
     * @throws ParseException 
     */
    private void addToIndex(String timeRange) throws ParseException {
        TimeRange tr = TimeRangeFormat.parse(timeRange);
        List<Object[]> counts = userTagDao.getTagCountsByEventBeginDate(tr);
        tagIndex.put(tr, makeTagCloud(counts));
        log.info("added to index " + counts.size() + " tags for time range " + timeRange);
    }

}
