<!DOCTYPE html>
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=utf8">
   <!-- title>netty-servlet-server</title -->
</head>
<body>
<h1>&nbsp;&nbsp;netty servlet server</h1>
<h3>&nbsp;&nbsp;&nbsp;&nbsp;A simple servlet server based on netty framework that can be used as the web container of springboot framework, such as Tomcat, Jetty and so on.
</h3>
<pre>
   @SpringBootApplication
   @RestController
   public class App
   {   
      @GetMapping("/about")
      public String about()
      {
         return "About: netty servlet server 1.1.2";
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
</pre>
<ul>
	<li><b>1.1.2</b> Implements some springboot configurable interfaces to configure the servlet web server.</li>
	<li><b>1.1.1</b> Implement interfaces such as ServletWebServerFactory to become a servlet web server that can be configured by springboot framework, such as Tomcat, Jetty and so on.</li>
	<li><b>1.1.0</b> Improves the interfaces and implementation classes used in the project, prepare to become a servlet web server that can be used by springboot configurations such as Tomcat, Jetty etc.</li>
	<li><b>1.0.3</b> Improves the interfaces and implementation classes used in the project.</li>
	<li><b>1.0.2</b> Reorganizes the interfaces and implementation classes used in the project.</li>
	<li><b>1.0.1</b> Implements a simple servlet server using the netty framework.</li>
</ul>
</body>
</html>
