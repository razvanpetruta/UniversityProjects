<html>

<head>
	<title>Library</title>
	<style>
		body {
			text-align: center;
		}

		.container {
			margin: 10rem 1rem;
		}
	</style>
</head>

<body>
	<div class="container">
		<h1>Hello!</h1>
		<?php
			session_start();
			if(isset($_SESSION['user'])) {
				echo '<p>Welcome, ' . $_SESSION['user'] . '! <br> <a href="adminPage.php">Go to dashboard page</a></p>';
			} else {
				echo '<p>In order to use this website, please login <a href="loginForm.php">here </a></p>';
			}
		?>
	</div>
</body>

</html>