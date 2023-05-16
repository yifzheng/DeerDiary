# Project Deer Diary

## Project Team 
| Name | Email | GitHub Username |
| :---: | :---: | :---: |
| Yifeng Zheng | yifeng.zheng43@bcmail.cuny.edu | yifzheng |
| Tianye Chen | tianyecf@gmail.com | tianye-chen |
| Collin Shi | cshi0418@gmail.com | collin-shi |
| Bohui Nong | bohui64@gmail.com | bohuinong |

# Deer Diary

### Digitize a traditional diary
- An Android application that can digitally take entries and to-do tasks

### createPost(content)
- Sends a POST request to the backend with the content as the payload.

### editPost(content, id)
- Send a PUT request to the backend with the content Id and content body and update the instance in the database.

### deletePost(id)
- Sends a DELETE request to the backend and deletes the post with the corresponding id from the backend.

### Reminder
- Each post has a due date and we can create an alert when the task has not been completed


## Risks and Mitigation description ... 
Risk: Technical issues such as bugs, crashes or compatibility issues
Mitigation: conduct thorough testing and QA

# Iteration #1: 2 weeks
| Issues | Points | Completed |
| :---: | :---: | :---: |
| [User login](https://github.com/cunychenhclass/cisc3171proj-group6/issues/2) | 5 | yes |
| [User Registration](https://github.com/cunychenhclass/cisc3171proj-group6/issues/1) | 5 | yes |
| [Create a diary entry](https://github.com/cunychenhclass/cisc3171proj-group6/issues/8) | 5| yes |
| [View Diary Entry](https://github.com/cunychenhclass/cisc3171proj-group6/issues/16) | 5 | yes |
| [Sorting list of entries](https://github.com/cunychenhclass/cisc3171proj-group6/issues/5) | 2 | N/A |

### Iteration Velocity = 4 issues * 5 points = 20
### Weekly Velocity = 20 / 2 weeks = 10 points/week
## Reflection
- The first iteration of this Android project attempts to implement the following issues: user registration, user login, sort/display entries, create entry, and view entry. The time span of this iteration was two weeks and within this timeframe, we were able to implement majority of these issues/features to create a mimimum viable product. Users are now able to create an account on our app, login with their account, view a list and indiviudal existing diary entries, and create a diary entry. The only feature we have yet to fully implement was the sorting feature that filters the way the list of entries are displayed. 
- Through this iteration, we have learned a lot regarding commuinication and productivity. In terms of communication, the clarity and responsefulness of the team members were not ideal especially the response time between a notice might be two to three hours later. In terms of productivity, while we might have completed most of the issues for our first iteration, there wasn't much done throughout the given time span. Consequently, work had to be rushed while quality control isn't ideal. With these lessons, we believe that we can improve our communication and productivity in the next iteration.
# Iteration 2: 2 Weeks
| Issues | Points | Completed |
| :---: | :---: | :---: |
| [View User Profile](https://github.com/cunychenhclass/cisc3171proj-group6/issues/23) | 3 | yes |
| [Edit User Profile](https://github.com/cunychenhclass/cisc3171proj-group6/issues/26) | 3 | yes |

### Iteration Velocity = 2 issues * 3 points = 6
### Weekly Velocity = 6 / 2 weeks = 3 points/week
## Reflection
- For this iteration, the two main agendas was the design pattern of the application and continuation of the implementation of app issues. For this iteration, we completed two isses which were viewing user profile and edit user profile. These issues were implemented to provide some customization for the users of the app. Lessons learned from the previous iteration was carried over to this iteration.
- The design pattern implemented in this iteration is the MVC design pattern. The User class represents the Model, the XML files, activity_view_user_profile.xml, acitivty_edit_user_profile.xml, and etc represents the View and App activities such as ViewUserPrfile.java and EditUserProfile.java represent the Controller. The model class holds the structure of the user object being created and used in the application while the XML files layout what information from the User class we wanted to display. When the application loads, we first retrieve the current UID of the logged in user from FireBaseAuth and retrieve the user information from the User collection. Once we retrieve the data, we create a local user object to be accessed and the activity then sets what gets displayed on the XML file. Any changes made in the activity is updated to the database which is in turn updated locally on the activity through a snap shot listener. This implementation structure is carried over to the DiaryEntry class which has the DiaryEntry class as the Model, the XML file as the View, and the activity and recycler view adapater as the controller.
- The design principle implemented in our application is the Depedency Inversion/Injection Principle where we try to keep the high level modules such as MainActivity, ViewUserProfile, and EditUserProfile dependent on Interaface methods rather than class methods. For the latter two activities, User class can implement a person interface which can have getter and setter methods for first and last name. This removes some dependencies of the activity on the User class. Secondly, every declaration of a user object will use the interface and instantiate with the class. If there is another class in the future that uses similar methods as the User class from the interface, we can easily upscale and implement it. 

# Iteration 3/ Final Iteration
| Issues | Points | Completed |
| :---: | :---: | :---: |
| [Edit Diary Entry](https://github.com/cunychenhclass/cisc3171proj-group6/issues/4) | 3 | yes |
| [Delete Diary Entry](https://github.com/cunychenhclass/cisc3171proj-group6/issues/3) | 3 | yes |
| [Sorting list of entries](https://github.com/cunychenhclass/cisc3171proj-group6/issues/5) | 2 | yes |

### Iteration Velocity = 2 issues * 3 points + (1 issue * 2 points) = 8 points
### Weekly Velocity = 8 points / 2 weeks = 4 points/week
# Reflection
- For this final iteration, we completed the implementation of edit and delteting an existing diary entry and sorting entries by title and date. We are familiar with the process so we were able to complete these issues on time. Additionally, to touch up the code, we tried to make the code DRY and make sure the tests for the issues were running as planned.
- The second design pattern implmented in this application was the Data Transfer Object design pattern or known as Value Object pattern. This design pattern is used in distributed systems and multi-layered architectures. This design pattern aims to deliver data between different layers of the application. In our app, we have two different layes: front-end, the app, and backend, firebase. This structure allows us to encapsulate all the necesary information needed to be stored and accessed by the front-end. It is structured to be easily serializable making it suitable for JSON or XML transmission. We can now store all data as an object in our database and be able to covert the data retriveed from the database back to an object.
# Burnout Chart
![burnout_chart](https://github.com/cunychenhclass/cisc3171proj-group6/assets/70163313/008945c6-3373-4af7-88e1-2ff3df9348fc)
# App Demo


https://github.com/cunychenhclass/cisc3171proj-group6/assets/70163313/5cdc09c3-50d5-41d8-b311-04947fddceef


