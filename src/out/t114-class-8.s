.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_a :	.space	0
	global_main_z :	.space	0
	StringLiteral_01: .asciiz "inside method c of Z"
	A_a_z :	.space	0
	StringLiteral_11: .asciiz "entering method a of A"
	StringLiteral_21: .asciiz "exiting method a of A"
	A_b_z :	.space	0
	StringLiteral_31: .asciiz "entering method b of A"
	StringLiteral_41: .asciiz "exiting method b of A"

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
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal A_a
		addi $sp, $sp, -4
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	Z_c:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_a:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, A_a_z
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal A_b
		addi $sp, $sp, -4
		la $t0, StringLiteral_21
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_b:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, A_b_z
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $t0, StringLiteral_31
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		jal Z_c
		addi $sp, $sp, 0
		la $t0, StringLiteral_41
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
