bits 32 

global start        

extern exit, fopen, fread, printf, fclose           
import exit msvcrt.dll   
import fopen msvcrt.dll
import fread msvcrt.dll   
import printf msvcrt.dll 
import fclose msvcrt.dll 

segment data use32 class=data
    file_name db "file.txt", 0
    access_mode db "r", 0
    file_descriptor dd -1
    text resb 100
    char_format db "%c", 0
    space db ' '
    letters db "abcdefghijklmnopqrstuwxyz", 0

segment code use32 class=code
    start:
        ; open the file for reading
        ; fopen(file_name, access_mode)
        push dword access_mode
        push dword file_name
        call [fopen]
        add esp, 4 * 2
        
        ; check if there was an error when openning the file
        cmp eax, 0
        je final
        
        ; save the file descriptor
        mov [file_descriptor], eax
        
        ; read the text from the file
        ; fread(location, size, count, file_descriptor)
        push dword [file_descriptor]
        push dword 100
        push dword 1
        push dword text
        call [fread]
        add esp, 4 * 4
        
        ; in eax we'll have the number of chars read
        ; mark the end of the file
        mov [text + eax], byte 0
        
        ; parse the string from left to right
        cld
        mov esi, text
        
        loop_1:
            mov eax, 0
            ; load into al a char at a time
            lodsb ; esi will be incremented automatically
            
            ; check if we reached the end of the string
            cmp al, 0
            je end_loop_1
            
            ; check if it is not space (that means it is an uppercase)
            cmp al, ' '
            je ignore_space
            
            ; we have 2 special cases for A and B
            cmp al, 'A'
            je special_case
            cmp al, 'B'
            je special_case
            
            ; here it is an uppercase letter
            add eax, 30 ; decode the message
            
            jmp over_special
            special_case:
            add eax, 56
            
            over_special:
            
            ; print the decoded character to the screen
            ; printf(format, parameter)
            push dword eax
            push dword char_format
            call [printf]
            add esp, 4 * 2
            
            jmp over
            
            ignore_space:
            ; print the space
            mov eax, 0
            mov al, [space]
            
            ; printf(format, parameter)
            push dword eax
            push dword char_format
            call [printf]
            add esp, 4 * 2
            
            over:
            jmp loop_1
        
        end_loop_1:
        
        ; close the file
        ; fclose(file_descriptor)
        push dword [file_descriptor]
        call [fclose]
        add esp, 4
        
        
        final:
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
