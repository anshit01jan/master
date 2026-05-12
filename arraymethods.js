//map function
let array = [2,3,4,5,6,7,8,9,10]
let num = array.map((element) => {
    element = element * 2
    return element
})

console.log(num)

//fahrenheit to celsius
let fahrenheit = [32, 45, 50, 60, 72, 90]
let celsius = fahrenheit.map((element) => {
    element = (element - 32) * (5 / 9)
    return element
})
console.log(celsius)


let employees = [
    { name: 'John', salary: 50000 },
    { name: 'Jane', salary: 60000 },
    { name: 'Jim', salary: 70000 },
    { name: 'Jack', salary: 80000 },
    { name: 'Jill', salary: 90000 }
]

let employee = employees.filter((element) => {
    return element.salary > 60000
})
console.log(employee)