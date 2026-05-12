async function getUserData(){
    try{
    const data = await fetch("https://reqres.in/api/users?page=2");
    if (data.ok) {
        const userData = await data.json();
        console.log(userData);
    }
}catch(error){
    console.error(error);
}
}

getUserData();