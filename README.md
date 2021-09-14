# Databases-Final-Project
pg. 3 
 
customers match, a menu option should appear listing all customers with the given last 
name  asking  the  user  to  choose  which  customer  has  initiated  the  service  request.  
Otherwise, the client application should provide the option of adding a new customer. If 
an existing customer is chosen, the client application should list all cars associated with 
that client providing the option to initiate the service request for one of the listed cars, 
otherwise a new car should be added along with the service request information for it. 
x  Close A Service Request 
This function will allow you to complete an existing service request. Given a service 
request number and an employee id, the client application should verify the information 
provided and attempt to create a closing request record. You should make sure to check 
for the validity of the provided inputs (i.e. does the mechanic exist, does the request exist, 
valid closing date after request date, etc.) 
x  List date, comment, and bill for all closed requests with bill lower than 100 
.List the customers that have paid less than 100 dollars for repairs based on their 
previous service requests. 
x  List first and last name of customers having more than 20 different cars 
Find how many cars each customer has counting from the ownership relation and 
discover who has more than 20 cars. 
x  List Make, Model, and Year of all cars build before 1995 having less than 50000 
miles 
Get the odometer from the service_requests and find all cars before 1995 having less 
than 50000 miles in the odometer. 
x  List the make, model and number of service requests for the first k cars with the 
highest number of service orders. 
Find for all cars in the database the number of service requests. Return the make, 
model and number of service requests for the cars having the k highest number of 
service requests. The k value should be positive and larger than 0. The user should 
provide this value. Focus on the open service requests. 
x  List the first name, last name and total bill of customers in descending order of 
their total bill for all cars brought to the mechanic. 
For all service requests find the aggregate cost per customer and order customers 
according to that cost. List their first, last name and total bill. 
 
We strongly recommended that you start early and allocate at least 25 hours per person to 
get it finished. Don’t forget that each group has to schedule a presentation to show the system 
pg. 4 
 
running with all its functionalities to the TA. Slots for the presentation will be available online 
on a first come-first served basis. 
  For this phase you will be evaluated based on the system requirements. Your GUI and 
source code will also be taken into consideration in your final evaluation. Groups that implement 
systems with user-friendly interfaces, extra functionalities and error handling (i.e. invalid values, 
wrong operations, meaningful messages) will receive an extra credit. A final report about your 
system along with its source code has to be submitted through iLearn. Please keep in mind that 
we have already prepared a set of data, which you can load in the database once you create it. 
The due date for this phase is December 14th 2018. 
1.1   Grading 
  Your contribution to this projec t will be graded based on the following characteristics: 
x  Phase 1 (30%) 
-  Conceptual Design (ER Diagram) 
-  This phase will be completed individually. You must submit your solutions on 
iLearn. 
x  Phase 2 (10%) 
-  Logical DB Design (Relational Database Schema) 
-  This phase will also be completed individually. You must submit your solutions 
on iLearn. 
x  Phase 3 (60%) 
-  Documentation of the project including details about your assumptions (10%) 
-  Implementation of SQL queries in the Client Application (30%) 
-  Physical DB Design (DB performance tuning indexes) (10%) 
-  Documentation  of  the  project  including  any  assumptions  that  you  have  made  
(10%) 
-  This  phase  will  be  performed  in  groups  of  TWO  students.  No  individual 
submissions are allowed. Choose your partner wisely because the final evaluation 
is based on the group performance! In your report explicitly enumerate the tasks 
that each member of your group was responsible for. If one of the group members 
does most of the work the grade will be proportional to the effort. If you are not 
able to find a partner, one will be assigned to you at random. Please e-mail the 
TAs immediately if you need help finding a partner. 
Extra credit will be given for good GUI design and interface, any dataset or schema 
changes/extensions, etc. (10%) 
2  Requirement Analysis 
1. Customer 
x  The customer has a first name, last name, phone number and address. 
x  Customers can own many different cars 
TBD
pg. 5 
 
x  Customers may bring any of their car for service 
x  Customers bring their car for service at a certain date indicating to the mechanic if 
there is any problem with the car 
 
2. Cars 
x  Cars have a unique VIN, a year, a make and a model 
x  A car has exactly one owner. 
x  A car may have many outstanding service requests. 
x  For each outstanding service request, the car has an odometer reading and the date 
the car was brought in by the customer. 
x  The service request will be closed when the car is fixed a mechanic 
 
3. Mechanic   
x  Mechanics have a first name, a last name, an employee id,  and some years of 
experience. 
x  Mechanics work on a single car at a time.  
x  When a mechanic fixes a car, he/she closes the service request indicating when it 
was closed, any comments they have and the final bill. 
x  Exactly one mechanic can create a service request. 
x  Both open and closed service requests need to be available at any time.
