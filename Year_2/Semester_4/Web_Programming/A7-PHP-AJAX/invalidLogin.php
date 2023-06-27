<html>

<head>

	<script type="text/javascript">
		function redirect() {
			window.location.href = 'loginForm.php';
		}
	</script>

	<style>
		body {
			text-align: center;
		}

		p {
			margin: 10rem;
		}
	</style>

</head>

<body onLoad="setTimeout('redirect()', 5000)">
	<p>
		Authentication failed.<br />
		You will be redirected to the login page in 5 seconds. Click <a href="loginForm.php">here</a> to get to the
		login
		page yourself.
	</p>
</body>

</html>