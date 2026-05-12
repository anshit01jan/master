const obj = {
    "name": "John",
    "age": 30,
    "city": "New York",
    "hobbies": ["reading", "traveling", "swimming"]
}

console.log(`Name: ${obj.name} age: ${obj.age} city: ${obj.city} hobbies: ${obj.hobbies}`)

//2 . using constructor function

function Car(name, type, year) {
    this.name = name;
    this.type = type;
    this.year = year;
}

const car1 = new Car("BMW", "SUV", 2020);
const car2 = new Car("Audi", "Sedan", 2021);

console.log(`Car1: ${car1.name} type: ${car1.type} year: ${car1.year}`);
console.log(`Car2: ${car2.name} type: ${car2.type} year: ${car2.year}`);
console.log(car1.name);
console.log(car2.name);

//3. using class

class Person {
    constructor(name, age, city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
}

const p1 = new Person("John", 30, "New York");
const p2 = new Person("Jane", 25, "Los Angeles");

console.log(`Person1: ${p1.name} age: ${p1.age} city: ${p1.city}`);
console.log(`Person2: ${p2.name} age: ${p2.age} city: ${p2.city}`);
console.log(p1.name);
console.log(p2.name);

//4. using object.create

const person = {
    name: "John",
    age: 30,
    city: "New York"
}

const person2 = Object.create(person);
person2.name = "Jane";
person2.age = 25;
person2.city = "Los Angeles";

console.log(`Person1: ${person.name} age: ${person.age} city: ${person.city}`);
console.log(`Person2: ${person2.name} age: ${person2.age} city: ${person2.city}`);
console.log(person.name);
console.log(person2.name);

//5. using factory function
function createPerson(name, age, city) {
    return {
        name: name,
        age: age,
        city: city
    }
}

const person3 = createPerson("Alice", 28, "Chicago");
const person4 = createPerson("Bob", 22, "Miami");

console.log(`Person3: ${person3.name} age: ${person3.age} city: ${person3.city}`);
console.log(`Person4: ${person4.name} age: ${person4.age} city: ${person4.city}`);
console.log(person3.name);
console.log(person4.name);