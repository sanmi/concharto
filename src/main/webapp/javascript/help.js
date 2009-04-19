/*
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
 */
  /** For use with livepipe.js control.modal code */
  function setupHelpPanels() {
    $$('.link_help').each(function(link){
        var modal = new Control.Modal(link, {
				className: 'helpbox',
        overlayOpacity: 0.7,
        position: 'relative', 
        offsetLeft: 20, 
        offsetTop: 20
        });
    });
	}

  /** From w3cschools.com */
  function getCookie(c_name)
  {
  if (document.cookie.length>0)
    {
    c_start=document.cookie.indexOf(c_name + "=");
    if (c_start!=-1)
      { 
      c_start=c_start + c_name.length+1; 
      c_end=document.cookie.indexOf(";",c_start);
      if (c_end==-1) c_end=document.cookie.length;
      return unescape(document.cookie.substring(c_start,c_end));
      } 
    }
  return "";
  }

  /** From w3cschools.com */
  function setCookie(c_name,value,expiredays)
  {
    var exdate=new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie=c_name+ "=" +escape(value)+
    ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
  }
