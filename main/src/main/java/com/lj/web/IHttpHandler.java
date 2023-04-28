/*
 * Copyright (c) 2023 LiangJ2.
 *
 * Licensed under the GNU General Public License v2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lj.web;

/**
 * The http handler interface.
 * @author LiangJ2
 *
 * @param <HttpRequestType> The http request type
 * @param <HttpResponseType> The http response type
 */
public interface IHttpHandler<HttpRequestType, HttpResponseType, ReturnType>
{
   /**
    * Handle the given request and write to the response.
    * @param request current request
    * @param response current response
    * @return indicates completion of request handling
    */
   ReturnType handle(HttpRequestType request, HttpResponseType response);
}
//======================= End Interface IHttpHandler ========================
