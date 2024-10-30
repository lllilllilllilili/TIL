Given the CITY and COUNTRY tables, query the sum of the populations of all cities where the CONTINENT is 'Asia'.

Note: CITY.CountryCode and COUNTRY.Code are matching key columns.

Input Format

The CITY and COUNTRY tables are described as

```sql
-- select sum(population) from (select * from country where CONTINENT = 'Asia');

select sum(city.population)
from city
join country
on city.countrycode = country.code
where country.continent = 'Asia'
```

Ketty gives Eve a task to generate a report containing three columns: Name, Grade and Mark. Ketty doesn't want the NAMES of those students who received a grade lower than 8. The report must be in descending order by grade -- i.e. higher grades are entered first. If there is more than one student with the same grade (8-10) assigned to them, order those particular students by their name alphabetically. Finally, if the grade is lower than 8, use "NULL" as their name and list them by their grades in descending order. If there is more than one student with the same grade (1-7) assigned to them, order those particular students by their marks in ascending order.

Write a query to help Eve.

```sql

/*
    Enter your query here and follow these instructions:
    1. Please append a semicolon ";" at the end of the query and enter your query in a single line to avoid error.
    2. The AS keyword causes errors, so follow this convention: "Select t.Field From table1 t" instead of "select t.Field From table1 AS t"
    3. Type your code immediately after comment. Don't leave any blank line.
*/

select 
    case 
        when Grade >= 8 then Name
        else 'NULL'
    end as Name, Grade, Marks 
from Students
join Grades 
on Marks BETWEEN Min_Mark and Max_Mark
order by 
    Grade desc,
    case when Grade >= 8 then Name end asc,
    case when Grade < 8 then Marks end asc;

```
