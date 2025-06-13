import api from '../config/axios';

export interface Note {
    id: number;
    content: string;
    noteType: NoteType;
    noteDate: string;
    createdAt: string;
    updatedAt: string;
}

export enum NoteType {
    MEETING = "MEETING",
    PHONE_CALL = "PHONE_CALL",
    EMAIL = "EMAIL",
    FOLLOW_UP = "FOLLOW_UP",
    GENERAL = "GENERAL",
    OTHER = "OTHER"
}

export const noteTypeLabels = {
    [NoteType.MEETING]: "Notatka ze spotkania",
    [NoteType.PHONE_CALL]: "Rozmowa telefoniczna",
    [NoteType.EMAIL]: "Korespondencja email",
    [NoteType.FOLLOW_UP]: "Dalsze działania",
    [NoteType.GENERAL]: "Notatka ogólna",
    [NoteType.OTHER]: "Inna"
};

export interface CreateNoteRequest {
    content: string;
    noteType: NoteType;
    noteDate: string;
    accountId: number;
}

export interface UpdateNoteRequest {
    content: string;
    noteType: NoteType;
    noteDate: string;
}

export const fetchNotesByAccount = async (accountId: number): Promise<Note[]> => {
    const response = await api.get(`/api/notes/account/${accountId}`);
    return response.data;
};

export const createNote = async (noteData: CreateNoteRequest): Promise<Note> => {
    const response = await api.post('/api/notes', noteData);
    return response.data;
};

export const updateNote = async (noteId: number, noteData: UpdateNoteRequest): Promise<Note> => {
    const response = await api.put(`/api/notes/${noteId}`, noteData);
    return response.data;
};

export const deleteNote = async (noteId: number): Promise<void> => {
    await api.delete(`/api/notes/${noteId}`);
}; 