
function init() {
	console.log("Login Page");


}

function loginButtonAction() {
	let username = document.getElementById("username").value.trim();
	let password = document.getElementById("password").value.trim();
	console.log(username," ",password);
	if(username.length==0 || password.length==0){
		alert("Username & Password Cannot be Empty");
	}
	else {
		let postBody = {};
		postBody["username"]=username;
		postBody["password"]=password;
		callAPI(baseURL+loginAPI,POST,postBody,loginCallback);
	}

}

function loginCallback(response) {
	console.log(response);
	try{
		let json = JSON.parse(response);
		document.cookie='Authorization='+json["token"];
		sessionStorage.setItem("token",json["token"]);
	}catch (e) {
		console.log(e.toString()+' Login Failed');
		alert(e.toString()+' Login Failed');
		//deleting token from cookie
		document.cookie = "Authorization= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
	}
	window.location.pathname = '/'+nav_buttons[0];
}







