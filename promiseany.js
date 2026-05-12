function f1(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            reject("Error from function 1");
        }, 2000);
    });
}

function f2(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            reject("Error from function 2");
        }, 2000);
    });
}

function f3(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            reject("Error from function 3");
        }, 2000);
    });
}

Promise.any([f1(), f2(), f3()])
    .then(result => {
        console.log("First fulfilled promise result:", result);
    })
    .catch(error => {
        console.error("All promises were rejected:", error);
    });