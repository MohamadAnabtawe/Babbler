# Overview
 One of the main problem that one faces when learns a new language is the lack of practice. A social network that gives you an opportunity to talk to the native language speakers might be an option. Recently, it became very widespread to organize different events and meetings, using the internet and the social media. It makes the organization easier, with less use of resources and it helps to control who participate in the event. The aim of this project is to help people find partners for talk sessions, according to their hobbies and other interests, and organize the talks, based on those mutual interests. 

# Algorithm
In this project I used a matching algorithm called Pareto optimal solutions, this algorithm gives the us the optimal solutions based on number of criteria, for this projects there were two criterias: Time and Distance, where the time is the time of the session from now (how much time left for this talk session to start), and the distance is the distance between the topics of the user's interest and the available ones.For example: let's say I'm intrested in football and I'm searching for a talk session in english with this topic, unfortunately there is no available session with topic of "football", but we have a session with topic of "basketball" 20 hours from now, and we have a session with topic of "health" 20 minutes from now, we see that the distance between "football" and "basketball" is close, but that doesn't mean this is the optimal solution because we have criteria which is the time, so because "heath" is colse to "football" and it's 20 minutes from now it will be a prefered solution eventhough "basketball is closer in distance but farther in time. 

# Technologies
#### back-end
I used a free web host for this project called 000webhost.com, php programming languagel, SQL commands, phpMyAdmin for database, Android Studio, java programming language, StringRequest class for sending requests to server using Volley.  
#### front-end
Android Studio, java programming language, XML for layouts, and some other external libraries.
