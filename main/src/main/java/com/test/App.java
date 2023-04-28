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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lj.web.netty.spring.boot.NettyServletWebServerFactory;

/**
 * 
 * @author LiangJ2
 *
 */
@SpringBootApplication
@RestController
public class App
{
   @Value("${server.port}")
   private String port;
   
   @GetMapping("/test")
   public String test(@RequestParam("name") String name)
   {
      return "Test: port=" + port + " name=" + name;
   }
   //---------------------------------------------------------------------------
   
   @GetMapping("/about")
   public String about()
   {
      return "About: netty servlet server 1.1.2 port=" + port;
   }  
   //---------------------------------------------------------------------------

   @Bean
   public ServletWebServerFactory servletWebServerFactory()
   {
      return new NettyServletWebServerFactory(); 
   }
   //---------------------------------------------------------------------------
   
   public static void main(String[] args)
   {
      SpringApplication.run(App.class, args);
   }
   //---------------------------------------------------------------------------
}
//============================== End Class App ==============================
