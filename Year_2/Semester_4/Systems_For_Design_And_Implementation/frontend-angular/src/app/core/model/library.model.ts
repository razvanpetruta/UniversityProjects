import { BookNoLibrary } from "./book.model"
import { ReaderMembership } from "./reader.model"

export interface Library {
    id: number,
    name: string,
    address: string,
    email: string,
    website: string,
    yearOfConstruction: number,
    username: string
}

export interface LibraryAll {
    id: number,
    name: string,
    address: string,
    email: string,
    website: string,
    yearOfConstruction: number,
    totalReaders: number,
    totalBooks: number,
    username: string
}

export interface LibraryDetails {
    id: number,
    name: string,
    address: string,
    email: string,
    website: string,
    yearOfConstruction: number,
    books: BookNoLibrary[],
    readers: ReaderMembership[],
    username: string
}

export interface LibraryMembership {
    id: number,
    name: string,
    address: string,
    email: string,
    website: string,
    yearOfConstruction: number,
    startDate: Date,
    endDate: Date,
    username: string
}

export interface LibraryCount {
    id: number,
    name: string,
    totalCount: number
}

export interface AddUpdateLibraryDTO {
    name: string,
    address: string,
    email: string,
    website: string,
    yearOfConstruction: number
}