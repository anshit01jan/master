let array = [34,56,23,78,67,90,234,28,9];
let max = array.reduce((acc,e) => {
    if(acc > e){
        return acc;
    }
    else{
        return e;
    }
},array[0]);

console.log(max);


function factorial(n){
    if(n ===0 || n === 1){
        return 1;
    }
    else{
        return n* factorial(n-1);
    }
}

console.log(factorial(5));

function fibonacci(n){
    let a=0, b=1, i=2;
    console.log(a);
    console.log(b);
    while(i < n){
        let c = a + b;
        console.log(c);
        a = b;
        b = c;
        i++;
    }
}
fibonacci(10);