bits 32
global start

extern exit, fopen, fwrite, fclose
import exit msvcrt.dll
import fopen msvcrt.dll
import fwrite msvcrt.dll
import fclose msvcrt.dll

segment data use32 class=data
    file_name db "my_file.txt", 0
    access_mode db "w", 0
    file_descriptor dd -1
    text db "Ana [are] *20* de Banane din 100%x", 0
    len equ $ - text - 1
    current_char db 0

segment code use32 class=code
start:
    ; A file name and a text (defined in data segment) are given. The text contains lowercase letters, uppercase letters, digits and special characters. Replace all spaces from the text with character 'S'. Create a file with the given name and write the generated text to file.
    
    ; fopen(char *file_name, char *access_mode)
    push dword access_mode
    push dword file_name
    call [fopen]
    add esp, 4 * 2
    
    ; check if there was an error
    cmp eax, 0
    je finish
    
    ; save the file descriptor
    mov [file_descriptor], eax
    
    ; for iterating the text
    mov ebx, 0
    
    my_loop:
        ; load a byte from the text
        mov al, byte [text + ebx]
        cmp al, ' '
        jne not_space
        mov [current_char], byte 'S'
        jmp step_over
        not_space:
            mov [current_char], al
        step_over:
        
        ; int fwrite(const void *ptr, int size, int count, FILE *descriptor)
        push dword [file_descriptor]
        push dword 1
        push dword 1
        push dword current_char
        call [fwrite]
        add esp, 4 * 4
        
        ; go to the next character from the string
        inc ebx
        
        ; if we reach the end of the string go to close file
        cmp ebx, len
        je close
        
        ; if there was an error close go to close file
        cmp eax, 0
        je close
    jmp my_loop
    
    close:
    ; fclose(FILE *descriptor)
    push dword [file_descriptor]
    call [fclose]
    add esp, 4
    
    finish:
    push dword 0
    call [exit]