export interface UserProfile {
  id: number;
  username: string;
  roles: [
    {
      name: string;
      id: number;
    }
  ];
  userProfile: {
    id: number;
    bio: string;
    location: string;
    birthDate: string;
    gender: string;
    maritalStatus: string;
  };
  totalLibraries: number;
  totalBooks: number;
  totalReaders: number;
}

export interface RegisterResponseDTO {
  username: string;
  jwtToken: string;
}

export interface UserInfo {
  id: number;
  bio: string;
  location: string;
  birthDate: string;
  gender: string;
  maritalStatus: string;
}

export interface UserWithRolesDTO {
  username: string,
  roles: [
    {
      id: number,
      name: string
    },
  ]
}

export interface UserRolesUpdate {
  roles: string[]
}

export interface SQLResponse {
  message: string
}
