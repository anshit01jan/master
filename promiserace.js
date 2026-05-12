function array1(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve([1, 2, 3]);
        }, 1000);
    });
}

function array2(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve([4, 5, 6]);
        }, 800);
    });
}

function array3(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            reject("Error in array3");
        }, 500);
    });
}

Promise.race([array1(), array2(), array3()])
    .then(result => {
        console.log("First resolved array:", result);
    })
    .catch(error => {
        console.error("Error:", error);
    });