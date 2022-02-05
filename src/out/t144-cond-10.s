.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_a :	.space	4
	StringLiteral_01: .asciiz "true"
	StringLiteral_11: .asciiz "false"
	A_field : .word 0

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
		jal A_init
		addi $sp, $sp, 0
		jal A_get_field
		addi $sp, $sp, 0
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 20
		addi $sp, $sp, -4
		lw $t1 0($sp)
		slt $t1, $t1, $t0
		move $t0, $t1
		beq $t0, 0, L1
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		j L1exit
L1:
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
L1exit:
		jal A_get_field
		addi $sp, $sp, 0
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 25
		addi $sp, $sp, -4
		lw $t1 0($sp)
		slt $t1, $t1, $t0
		move $t0, $t1
		beq $t0, 0, L2
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		j L2exit
L2:
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
L2exit:
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_init:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, A_field
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $t0, 22
		sw $t0, 0($a3)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_get_field:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, A_field
		lw $t0, 0($a0)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
