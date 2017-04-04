# restStatisticsDemo
restfull API for statistics



## Run
`mvn spring-boot:run`

Test page

http://127.0.0.1:8080/index.html

## Limitations

I assume that a 1 second precision would be enough and 
if not, you can change CAPACITY and DIVISOR constants 
(for example for 1 millisecond: CAPACITY=60000 DIVISOR=1).


