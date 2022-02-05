.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_a :	.space	4
	global_main_z :	.space	0
	Z_c_a :	.space	4
	StringLiteral_01: .asciiz "entering method c of Z"
	A_counter : .word 0
	StringLiteral_11: .asciiz "entering method a of A"
	StringLiteral_21: .asciiz "Value of counter is: "

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	runtime_error:
		li $v0, 4
		la $a0, error_run_time
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a3, 0($a0) 
		la $a3, 0($a0) 
		jal A_init
		addi $sp, $sp, 0
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal Z_c
		addi $sp, $sp, -4
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	Z_c:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, Z_c_a
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		jal A_a
		addi $sp, $sp, 0
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_init:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, A_counter
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $t0, 0
		sw $t0, 0($a3)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_a:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, A_counter
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $a0, A_counter
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 1
		addi $sp, $sp, -4
		lw $t1 0($sp)
		add $t1, $t1, $t0
		move $t0, $t1
		sw $t0, 0($a3)
		jal A_print
		addi $sp, $sp, 0
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_print:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $t0, StringLiteral_21
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $a0, A_counter
		lw $t0, 0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
