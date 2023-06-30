import { Library } from "./library.model"

export interface Book {
    id: number,
    title: string,
    author: string,
    publisher: string,
    price: number,
    publishedYear: number,
    libraryID: number,
    description: string,
    username: string
}

export interface BookNoLibrary {
    id: number,
    title: string,
    author: string,
    publisher: string,
    price: number,
    publishedYear: number,
    description: string,
    username: string
}

export interface BookDetails {
    id: number,
    title: string,
    author: string,
    publisher: string,
    price: number,
    publishedYear: number,
    description: string,
    library: Library,
    username: string
}

export interface UpdateBookDTO {
    title: string,
    author: string,
    publisher: string,
    price: number,
    publishedYear: number,
    description: string
}

export interface AddBookDTO {
    title: string,
    author: string,
    publisher: string,
    price: number,
    publishedYear: number,
    description: string
}