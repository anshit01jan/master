const square = n => n * n;
console.log(square(5));

const emp = {
    name: 'John',
    name2: 'Doe'
}

const getEmp =  emp => emp.name + ' ' + emp.name2;
console.log(getEmp(emp));

const maxNum =  (...num) => {
    return num.reduce((acc, e) => {
        if(acc > e){
            return acc;
        }
        else{
            return e;
        }
    }, num[0]);
}

console.log(maxNum(34,56,23,78,67,90,234,28,9));