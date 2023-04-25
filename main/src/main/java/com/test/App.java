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

package com.test;

/**
 * 
 * @author LiangJ2
 *
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
@org.springframework.web.bind.annotation.RestController
public class App
{
   @org.springframework.beans.factory.annotation.Value("${server.port}")
   private String port; // = "8080";
   
   @org.springframework.web.bind.annotation.GetMapping("/test")
   public String test(@org.springframework.web.bind.annotation.RequestParam("name") String name)
   {
      return "test: port=" + port + " name=" + name;
   }
   //---------------------------------------------------------------------------
   
   @org.springframework.web.bind.annotation.GetMapping("/about")
   public String about()
   {
      return "about: netty-servlet-server 1.1.1 port=" + port;
   }  
   //---------------------------------------------------------------------------

   @org.springframework.context.annotation.Bean
   public org.springframework.boot.web.servlet.server.ServletWebServerFactory servletWebServerFactory()
   {
      return new com.lj.web.netty.springframework.boot.NettyServletWebServerFactory(Integer.parseInt(port)); 
   }
   //---------------------------------------------------------------------------
   
   public static void main(String[] args)
   {
      org.springframework.boot.SpringApplication.run(App.class, args);
   }
   //---------------------------------------------------------------------------
}
//============================== End Class App ==============================
