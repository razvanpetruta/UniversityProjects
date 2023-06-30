import { LibraryMembership } from "./library.model"

export interface Reader {
    id: number,
    name: string,
    email: string,
    birthDate: Date,
    gender: string,
    student: boolean,
    username: string
}

export interface ReaderAll {
    id: number,
    name: string,
    email: string,
    birthDate: Date,
    gender: string,
    student: boolean,
    totalLibraries: number,
    username: string
}

export interface ReaderDetails {
    id: number,
    name: string,
    email: string,
    birthDate: Date,
    gender: string,
    student: boolean,
    libraries: LibraryMembership[],
    username: string
}

export interface ReaderMembership {
    id: number,
    name: string,
    email: string,
    birthDate: Date,
    gender: string,
    student: boolean,
    startDate: Date,
    endDate: Date
}

export interface AddUpdateReaderDTO {
    name: string,
    email: string,
    birthDate: Date,
    gender: string,
    student: boolean
}

export interface Membership {
    id: {
        libraryID: number,
        readerID: number
    },
    startDate: Date,
    endDate: Date
}
