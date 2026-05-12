function getRandomNumber(){
    return new Promise((resolve,reject)=>{
        setTimeout(()=>{
            const num = Math.floor(Math.random() * 10);
            if(num > 5){
                resolve(num);
            }else{
                reject(`${num} is less than 5`);
            }
        }, 1000);
    })
}

try{
    const result = await getRandomNumber();
    console.log(result);
}catch(error){
    console.error(error);
}