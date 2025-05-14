# CLI Expense Tracker (Java 21 + Gradle)

The CLI Expense Tracker is an easy way for users to keep track of their finances and expenses. 

## Features
1. Open or create a new JSON expense file.
2. Add a new expense to your expense file.
3. List expenses by date or amount/value in increasing or decreasing order.
4. Filter expenses by ther amount/value, category, and date.
5. Update expenses.
6. Delete expenses.
7. Create a summary for a specific month.

## Requirements
- Java 21+

## Getting Started
1. Download the **ExpenseTracker.jar** from the releases section
2. In your terminal write: `java -jar *ExpenseTracker.jar`
3. You're Done!

## How To Use
- Before performing any action, you must first open your expense file: `java -jar *ExpenseTracker.jar open [filename]`
- If the expense file with a give filename is not found, a new exepense file with the filename is created. 
- Enter any command after:
  1. add -> example: `open data.json add -value 42.75 -desc "Grocery run" -date 2025-05-11 -category food`
  2. list -> example: `open data.json list -sort_asc amount`
  3. filter -> example: `open data.json filter -category food -from 2025-01-01 -to 2025-03-31 -min 10`
  4. update -> example: `open data.json update -id 7 -value 19.99 -desc "Corrected name"`
  5. delete -> example: `open data.json delete -id 7`
  6. summary -> example: `open data.json summary -month 2025-05`
