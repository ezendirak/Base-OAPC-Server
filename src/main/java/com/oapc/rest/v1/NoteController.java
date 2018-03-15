package com.oapc.rest.v1;

import com.oapc.model.Note;
import com.oapc.repo.NoteRepository;
import com.oapc.model.ErrorRest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1")
public class NoteController {
	
	private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    NoteRepository noteRepository;

    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////            
    // http://localhost:8080/api/v1/notes_page?page=1per_page=3
    // https://www.callicoder.com/spring-boot-rest-api-tutorial-with-mysql-jpa-hibernate/
    // https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
    // https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/HttpStatus.html
    // http://www.deadcoderising.com/2017-02-21-java-8-accumulate-your-streams-using-collectors/
    
    @Transactional(readOnly = true)
    @GetMapping("/notes_page")
    public ResponseEntity<?> getAllNotesPage(
    		@RequestParam(value="page",     defaultValue="0") String spage,
    		@RequestParam(value="per_page", defaultValue="0") String sper_page
    		)	    		    		    	
    {
   	 	 Integer page      = Integer.parseInt(spage);
   	 	 Integer per_page  = Integer.parseInt(sper_page);
   	 	 
	     if (page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("page not defined"), HttpStatus.BAD_REQUEST);
   	 	     	    	
	     if (per_page == 0)
	    	 return new ResponseEntity<ErrorRest> (new ErrorRest("per_page not defined"), HttpStatus.BAD_REQUEST);	     
	     //
    	 Stream<Note> stream_cont = noteRepository.findAllStream();
    	 
    	 Long    total_reg = stream_cont.count();    	     	    
    	 Integer skip_reg  = (page - 1) * per_page;
    	
    	 Stream<Note> stream_data = noteRepository.findAllStream();    	     	     	
	     
	     logger.info("page=" + page.toString() + ",per_page=" + per_page.toString() + ",total_reg=" +  total_reg.toString() + ",skip_reg=" + skip_reg.toString());	    
	    	    	     
		 return new ResponseEntity<List<Note>> (stream_data.skip(skip_reg).limit(per_page).collect(Collectors.toList()), HttpStatus.OK);
    }    
        
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Transactional(readOnly = true)
    @GetMapping("/notes_count")
    public Long getNotesCount()	    		    		    	
    {    	    	 
    	 Stream<Note> stream_cont = noteRepository.findAllStream();   
    	 
    	 return stream_cont.count();	     
    }
    
    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(note);
    }

    @PostMapping("/notes")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteRepository.save(note);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody Note noteDetails) {
    	
        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }
        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(note);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Note> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findOne(noteId);
        if(note == null) {
            return ResponseEntity.notFound().build();
        }

        noteRepository.delete(note);
        return ResponseEntity.ok().build();
    }
}