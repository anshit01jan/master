function f1(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve("Result from function 1");
        }, 2000);
    });
}


function f2(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve("Result from function 2");
        }, 2000);
    });
}

function f3(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            reject("Error in function 3");
        }, 2000);
    });
}

Promise.allSettled([f1(), f2(), f3()])
    .then(results => {
        results.forEach((result, index) => {
            if (result.status === 'fulfilled') {
                console.log(`Function ${index + 1} executed successfully with result: ${result.value}`);
            } else {
                console.error(`Function ${index + 1} failed with reason: ${result.reason}`);
            }
        });
    })