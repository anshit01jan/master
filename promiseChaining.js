function getNum(num){
    return new Promise((resolve,reject)=>{
    setTimeout(()=>{
        if(num > 5){
            resolve(num);
        }
        else{
            reject(new Error("Number is less than 5"));
        }
    }, 3000);
})
}

getNum(Math.floor(Math.random() * 10))
    .then(result => {
        console.log("Promise resolved with number: " + result);
        return getNum(Math.floor(Math.random() * 10));
    })
    .then(result => {
        console.log("Promise resolved with number: " + result);
        return getNum(Math.floor(Math.random() * 10));
    })
    .catch(error=>{
        console.log("Promise rejected with error: " + error.message);
    })