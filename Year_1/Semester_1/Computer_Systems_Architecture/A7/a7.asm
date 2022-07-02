bits 32
global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment data use32 class=data
    a dd 0
    a_format db "%d", 0
    b dd 0
    b_format db "%d", 0
    k equ 2
    a_equal db "a = ", 0
    b_equal db "b = ", 0
    sol db "(a - b) * k = %llx (h)", 0

segment code use32 class=code
start:
    ; Two numbers a and b are given. Compute the expression value: (a-b)*k, where k is a constant value defined in data segment. Display the expression value (in base 16).
    
    ; get the value of a
    ; print a = 
    push dword a_equal
    call [printf]
    add esp, 4 * 1
    
    ; read a from keyboard
    push a
    push a_format
    call [scanf]
    add esp, 4 * 2
    
    ; get the value of b
    ; print b = 
    push dword b_equal
    call [printf]
    add esp, 4 * 1
    
    ; read b from keyboard
    push b
    push b_format
    call [scanf]
    add esp, 4 * 2
    
    ; a - b
    mov eax, [a]
    sub eax, [b]
    mov ebx, k
    ; (a - b) * k
    imul ebx
    
    push edx
    push eax
    push dword sol
    call [printf]
    add esp, 4 * 3
    
    push dword 0
    call [exit]