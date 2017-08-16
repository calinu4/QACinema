function a() {
    var exampleInputEmail2 = document.getElementById("exampleInputEmail2").value
    var exampleInputPassword2 = document.getElementById("exampleInputPassword2").value
    window.alert(exampleInputEmail2+exampleInputPassword2)

    $.post("login",{email:exampleInputEmail2,password:exampleInputPassword2}, function (data) {
        alert("Data Loaded: " + data);
    });
}

function sendLogin() {
    window.alert("#")
    $.ajax
    ({
        type: "POST",
        //the url where you want to sent the userName and password to
        url: 'login',
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        async: false,
        //json object to sent to the authentication url
        data: JSON.stringify({
            "email": document.getElementById("exampleInputEmail2").value,
            "password": document.getElementById("exampleInputPassword2").value
        }),
        success: function (){
            alert("Thanks!");
        }
    })
}