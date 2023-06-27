export interface Book {
    id: number,
    title: string,
    author: string,
    noPages: number,
    price: number,
    genre: string
}

export interface RentedBook {
    id: number,
    user: {
        username: string,
    },
    book: {
        title: string
    },
    startDate: string,
    endDate: string,
    approved: number
}