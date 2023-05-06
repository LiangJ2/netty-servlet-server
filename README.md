# netty servlet server

<h3>&nbsp;&nbsp;&nbsp;&nbsp;A simple servlet server based on netty framework that can be used as the web container of springboot framework, such as Tomcat, Jetty and so on.
</h3>

# Example

```java
@SpringBootApplication
@RestController
public class App
{
   @GetMapping("/about")
   public String about()
   {
      return "About: netty servlet server 1.2.0";
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
```

# Releases

<ul>
	<li><b>1.2.0</b> Supports HTTPS requests.</li>
	<li><b>1.1.2</b> Implements some springboot configurable interfaces to configure the servlet web server.</li>
	<li><b>1.1.1</b> Implement interfaces such as ServletWebServerFactory to become a servlet web server that can be configured by springboot framework, such as Tomcat, Jetty and so on.</li>
	<li><b>1.1.0</b> Improves the interfaces and implementation classes used in the project, prepare to become a servlet web server that can be used by springboot configurations such as Tomcat, Jetty etc.</li>
	<li><b>1.0.3</b> Improves the interfaces and implementation classes used in the project.</li>
	<li><b>1.0.2</b> Reorganizes the interfaces and implementation classes used in the project.</li>
	<li><b>1.0.1</b> Implements a simple servlet server using the netty framework.</li>
</ul>

# Enjoy netty servlet server!

- LiangJ2 (original author)
