<html>

<head>
    <title>Log In</title>
    <style>
        body {
			text-align: center;
            position: relative;
            width: 100%;
		}

		.container {
			margin: 10rem 30rem;
		}

        td {
            padding-top: 1rem;
        }
    </style>
</head>

<body>

    <div class="container">
        <form method="POST" action="login.php">
            <table>
                <tr>
                    <td>User:</td>
                    <td><input type="text" name="user" /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" /></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Login" /></td>
                </tr>
            </table>
        </form>
    </div>

</body>

</html>