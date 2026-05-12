function f1(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            console.log("Function 1 executed");
            resolve("Result from function 1");

        }, 2000);
    });
}

function f2(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            console.log("Function 2 executed");
            resolve("Result from function 2");

        }, 2000);
    });
}

function f3(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            console.log("Function 3 executed");
            resolve("Result from function 3");

        }, 2000);
    });
}

function f4(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            console.log("Function 4 not executed");
            reject("Error in function 4");

        }, 2000);
    });
}


Promise.all([f1(), f2(), f3(), f4()])
    .then(results => {
        console.log("All functions executed successfully");
        console.log(results); // Array of results from all promises
    })
    .catch(error => {
        console.error("An error occurred:", error);
    });