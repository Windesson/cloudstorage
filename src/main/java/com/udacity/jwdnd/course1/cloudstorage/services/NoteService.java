package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteModel;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper){ this.noteMapper = noteMapper;}

    public NoteModel getNotes(Integer userId) {
        return noteMapper.getNotes(userId);
    }

    public Integer deleteNote(Integer noteId, Integer userId) {
        return noteMapper.deleteNote(noteId, userId);
    }

    public Integer addNote(NoteModel noteModel) {
        return noteMapper.addNote(noteModel);
    }
}
