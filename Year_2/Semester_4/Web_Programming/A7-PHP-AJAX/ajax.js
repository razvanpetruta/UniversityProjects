$(document).ready(function() {
	getGenres();
	getRentedBooks();
	getUnapprovedRentals();

	$('#filteredBooks').on('click', '.delete-book', function() {
		var bookId = $(this).data('id');
		if (confirm('Are you sure you want to delete this book?')) {
			$.ajax({
				type: "POST",
				url: "deleteBook.php",
				data: { id: bookId },
				success: function() {
					selectGenre(selectedGenre);
				},
				error: function() {
					alert('Error deleting book');
				}
			});
		}
	});

	$('#filteredBooks').on('click', '.edit-book', function() {
		var bookId = $(this).data('id');
		window.location.href = 'editBook.php?id=' + bookId;
	});

	$('.unapprovedRentals').on('click', '.approve-rental', function() {
		var rentalId = $(this).data('id');
		$.ajax({
			type: "POST",
			url: "approveRental.php",
			data: { id: rentalId },
			success: function(response) {
				alert(response);
				getUnapprovedRentals();
				getRentedBooks();
			},
			error: function() {
				alert('Error approving rental');
			}
		});
	});

	$('.unapprovedRentals').on('click', '.reject-rental', function() {
		var rentalId = $(this).data('id');
		$.ajax({
			type: "POST",
			url: "rejectRental.php",
			data: { id: rentalId },
			success: function(response) {
				alert(response);
				getUnapprovedRentals();
				getRentedBooks();
			},
			error: function() {
				alert('Error approving rental');
			}
		});
	});

	$('#filteredBooks').on('click', '.rent-book', function() {
		var bookId = $(this).data('id');
		$.ajax({
			type: "POST",
			url: "requestBook.php",
			data: { id: bookId },
			success: function(request) {
				alert(request);
				getRentedBooks();
			},
			error: function() {
				alert('Error requesting book');
			}
		});
	});
});

function getGenres() {
	$.ajax({
		type: "GET",
		url: 'getGenres.php',
		success: function(responseData) {
			var genres = JSON.parse(responseData);
			var genresSelect = document.getElementById('genre');
			for (var i = 0; i < genres.length; i++) {
				var genreOption = document.createElement('option');
				genreOption.text = genres[i];
				genreOption.value = genres[i];
				genresSelect.add(genreOption);
			}
			if (genres.length) {
				selectGenre(genres[0]);	
			}
		}
	});
}

function getRentedBooks() {
	$.ajax({
		type: "GET",
		url: 'getRentedBooks.php',
		success: function(responseData) {
			$('.rentedBooks').html(responseData);
		}
	});
}

function getUnapprovedRentals() {
	$.ajax({
		type: "GET",
		url: 'getUnapprovedRentals.php',
		success: function(responseData) {
			$('.unapprovedRentals').html(responseData);
		}
	});
}

var selectedGenre;

function selectGenre(genre) {
	selectedGenre = genre;
	$.ajax({
		type: "GET",
		url: 'getFilteredBooks.php',
		data: { genre: genre },
		success: function(responseData) {
			$('#filteredBooks').html(responseData);
		}
	});
}