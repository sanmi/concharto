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

import java.util.List;
import java.util.SortedMap;

import org.tsm.concharto.model.time.TimeRange;

public class CatalogTagCloud {
    List<TagCloudEntry> recent;
    SortedMap<TimeRange, List<TagCloudEntry>> tagIndex;
	public List<TagCloudEntry> getRecent() {
		return recent;
	}
	public void setRecent(List<TagCloudEntry> recent) {
		this.recent = recent;
	}
	public SortedMap<TimeRange, List<TagCloudEntry>> getTagIndex() {
		return tagIndex;
	}
	public void setTagIndex(SortedMap<TimeRange, List<TagCloudEntry>> tagIndex) {
		this.tagIndex = tagIndex;
	}

}
