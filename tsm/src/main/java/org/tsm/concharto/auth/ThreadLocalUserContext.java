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
package org.tsm.concharto.auth;

/**
 * Static class for saving user information as ThreadLocal info.  This
 * is used by the audit interceptor and others. 
 * 
 * @author frank
 *
 */
public class ThreadLocalUserContext {
    private static ThreadLocal<UserContext> localUserContext = new ThreadLocal<UserContext>();

    public static UserContext getUserContext() {
        UserContext context = (UserContext) localUserContext.get();
        if (context == null) {
            context = new UserContext();
            localUserContext.set(context);
        }
        return context;
    }

    public static void setUserContext(UserContext context) {
        localUserContext.set(context);
    }
}
