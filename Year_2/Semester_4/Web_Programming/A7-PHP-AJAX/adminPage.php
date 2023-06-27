<?php
    session_start();

    $user = $_SESSION['user'];

    if (!isset($user)) {
        header("Location: loginForm.php");
        return;
    }
?>

<html>

<head>
    <title>Article</title>

    <style>
        body {
            text-align: center;
            margin-bottom: 2rem;
        }

        .container {
            margin-top: 2rem;
        }

        .content {
            margin-top: 3rem;
        }

        table {
            margin: 2rem auto;
            border-collapse: collapse;
        }

        td,
        th {
            border: 1px solid black;
            padding: 1rem;
        }
    </style>
</head>

<body>
    <div class="container">
        <div>
            Hello,
            <?php echo $_SESSION['user']; ?>
        </div>
        <a class="logout" href="logout.php">Log out</a>

        <br>
        <a href="index.php">back</a>
    </div>

    <div class="content">

        <?php
            $role = $_SESSION['role'];

            if ($role == 'USER') {
                echo "<h3>Approved rentals:</h3><div class='rentedBooks'></div><br>";
            }

            if ($role == 'ADMIN') {
                echo "<h3>Current rentals:</h3><div class='rentedBooks'></div><br>";
            }
        ?>

        <?php
            $role = $_SESSION['role'];

            if ($role == 'ADMIN') {
                echo "<h3>Rentals to approve:</h3><div class='unapprovedRentals'></div><br>";
            }
        ?>

        <?php
            $role = $_SESSION['role'];

            if ($role == 'ADMIN') {
                echo "<a href='addBook.php'>Add new book</a><br><br>";
            }
        ?>

        <h3>Search books:</h3>
        <label>Choose book genre:&nbsp;</label>
        <select id="genre" onChange="selectGenre(this.options[this.selectedIndex].value)">
        </select>
        <div id="filteredBooks"></div>
    </div>

    <script type="text/javascript" src="jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="ajax.js"></script>
</body>

</html>