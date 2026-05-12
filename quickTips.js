//1. distinct values in array
const arr = [1,2,3,3,4,5,5,6];
const distinctArr = [...new Set(arr)];
console.log(distinctArr); // Output: [1, 2, 3, 4, 5, 6]

//2. convert int to string
const num = 123;
const str = num + "";
const s = str+10;
console.log(s); // Output: "12310"

//3. convert float to int
const  floatNum = 12.345;
const intNum = parseInt(floatNum);
console.log(intNum); // Output: 12

//4. check if value is a number
const num1 = 42;
if(typeof(num1) === 'number' && !isNaN(num1)){
    console.log("value is a number");
}

//5. swap number
let a = 5, b = 10;
[a,b] = [b,a];
console.log(a, b); // Output: 10 5

//6. check if object has property.
const obj = {
    name: "John",
    age: 30
}
if(obj.hasOwnProperty('name')){
    console.log("Object has property 'name'");
}

//7. remove falsy values from array
const mixedArr = [0, 1, false, 2, '', 3];
const finalArr = mixedArr.filter(Boolean);
console.log(finalArr); // Output: [1, 2, 3]

//8. string to uppercase and lowercase
const str1 = "This is a Test String";
const upperString = str1.toLowerCase();
const lowerString = str1.toUpperCase();
console.log(upperString); // Output: "this is a test string"
console.log(lowerString); // Output: "THIS IS A TEST STRING"

//9. check if arrat contains value
const checkArr = [1, 2, 3, 4, 5];
const containsValue = checkArr.includes(4);
console.log(containsValue); // Output: true

//10. check if array is empty
const emptyArr = [];
const isEmpty = emptyArr.length === 0;
console.log(isEmpty); // Output: true

//11. generete random number
const min = 1;
const max = 100;
const randomNum = Math.floor(Math.random() * (max - min +1))+min;
console.log(randomNum); // Output: Random number between 1 and 100

//12. convert string to float
const floatStr = "12.34";
const floatValue = parseFloat(floatStr);
console.log(floatValue); // Output: 12.34

//13. join array elements
const joinArr = ['Hello', 'World'];
const joinedArray = joinArr.join(' ');
console.log(joinedArray); // Output: "Hello World"

//14. find max and min in array
const numbers = [5, 10, 15, 20];
const maxNum = Math.max(...numbers);
const minNum = Math.min(...numbers);
console.log(maxNum);
console.log(minNum); // Output: 20, 5

//15. get object property names
const person = {
    name: "Alice",
    age: 25,
    city: "New York"
};
const propertyNames = Object.keys(person);
console.log(propertyNames); // Output: ["name", "age", "city"]
const propertyValues = Object.values(person);
console.log(propertyValues); // Output: ["Alice", 25, "New York"]
const keyValueEntries = Object.entries(person);
console.log(keyValueEntries); // Output: [["name", "Alice"], ["age", 25], ["city", "New York"]]

//16. clone an array or object
const originalArr = [1, 2, 3];
const clonedArr = [...originalArr];
console.log(clonedArr); // Output: [1, 2, 3]
const originalObj = { a: 1, b: 2 };
const clonedObj = { ...originalObj };
console.log(clonedObj); // Output: { a: 1, b: 2 }

//17. convert object to array
const arrayObj = {
    name: "Bob",
    age: 30,
    city: "Los Angeles"
}
const objArray = Object.entries(arrayObj);
console.log(objArray); // Output: [["name", "Bob"], ["age", 30], ["city", "Los Angeles"]]

//18. fetch current date and time.
const currDateTime = new Date().toLocaleString();
console.log(currDateTime); // Output: Current date and time

//19. truncate an array.
const arr1 = [1, 2, 3, 4, 5];
arr1.length = 3;
console.log(arr1); // Output: [1, 2, 3]

//20. last item in array
const lastItem = arr1.slice(-1)[0];
console.log(lastItem); // Output: 3