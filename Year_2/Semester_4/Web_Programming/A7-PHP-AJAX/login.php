<?php  
    $formUser = $_POST['user'];
    $formPassword = $_POST['password'];
    $role;

    $link = mysqli_connect("localhost", "root", "", "testdb");

    $stmt = mysqli_prepare($link, "SELECT username, password, role FROM users WHERE username = ? and password = ?");
    mysqli_stmt_bind_param($stmt, "ss", $formUser, $formPassword);
    
    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $dbUser, $dbPassword, $role);

    if (!mysqli_stmt_fetch($stmt)) {
        header("Location: invalidLogin.php");
        return;
    }	

    mysqli_stmt_close($stmt);
    mysqli_close($link);

    session_start(); 

    $_SESSION['user'] = $formUser;
    $_SESSION['role'] = $role;
	
    header("Location: adminPage.php");	
?>
