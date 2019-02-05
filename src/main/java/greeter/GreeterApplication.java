/*
 * Copyright 2017-Present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greeter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class GreeterApplication {

    @Autowired
    MessagesClient messagesClient;

    @Value("${vcap.application.instance_id}")
    String instance_id;

    @RequestMapping(value = "/hello", method = GET)
    public String hello(@RequestParam(value="salutation", defaultValue="Hello") String salutation, 
                        @RequestParam(value="name", defaultValue="Bob") String name,
                        @RequestParam(value="instance", required=false) boolean showInstance) {
      Greeting greeting =  messagesClient.greeting(name, salutation);

      if(showInstance){
          return greeting.getMessage() + " from instance = " + instance_id + " hello world ";
      }
      return greeting.getMessage();
    }

    public static void main(String[] args) {
        SpringApplication.run(GreeterApplication.class, args);
    }
}
