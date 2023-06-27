export interface Book {
    ID: number,
    title: string,
    author: string,
    noPages: number,
    price: number,
    genre: string
}

export interface RentedBook {
    ID: number,
    title: string,
    username: string,
    startDate: string,
    returnDate: string,
    approved: number
}