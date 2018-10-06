# Inventory App

This Android application is created for Udacity's Android Basics Nanodegree program.
The purpose of the Inventory App is to track the inventory of a retail store.
Check lists are derived from the project rubrics created by Udacity.

# Stage 1 Check List
- [x] A main activity that has methods to read data, a Contract Java class, and a DbHelper Java class

- [x] A contract class that defines name of table and constants
- [x] One inner class for each table in the contract class
- [x] Constants for the Product Name, Price, Quantity, Supplier Name, and Supplier Phone Number in the contract class

- [x] A subclass of SQLiteOpenHelper that overrides onCreate() and onUpgrade()

- [x] Single insert method that adds Product Name, Price, Quantity, Supplier Name, Supplier Phone Number
- [x] At least 2 different datatypes

- [x] Single method that uses a Cursor from the database to perform a query on the table to retrieve at least one column of data
- [x] Said method should close the Cursor after reading from it

# Stage 2 Check List

- [x] The following features: Add Inventory, View Product Details, Edit Product Details, List of All Inventory
- [x] Use of Navigation Drawer, View Pager, Up/Back Navigation, or Intents to navigate between activities or fragments

- [x] Main Activity contains List of All Inventory
- [x] List item contains product name, price, quantity, sale button to reduce product quantity by one

- [x] Product Details layout displays product name, price, quanitity, supplier name, and supplier phone number stored in database
- [ ] Product Details layout contains buttons to increase and decrease available quantity
- [ ] Product Details layout contains button to order from supplier. This button should have an intent to a phone app to dial the supplier phone number from the database

- [x] Empty view should be a TextView with instructions on how to add items to the database
- [x] Add Product Button

- [x] Data validation to make sure no null (or invalid) values are accepted while adding or updating inventory