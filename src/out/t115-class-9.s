.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_a :	.space	0
	A_a_x : .word 0
	StringLiteral_01: .asciiz "entering method a of A"
	A_b_x : .word 0
	StringLiteral_11: .asciiz "entering method b of A"

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
		li $t0, 1
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal A_b
		addi $sp, $sp, -4
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal A_a
		addi $sp, $sp, -4
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal A_b
		addi $sp, $sp, -4
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal A_a
		addi $sp, $sp, -4
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
	A_a:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, A_a_x
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
		la $a0, A_a_x
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 1
		addi $sp, $sp, -4
		lw $t1 0($sp)
		add $t1, $t1, $t0
		move $t0, $t1
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	A_b:
		sw $ra,0($sp)
		addi $sp,$sp,-4
		la $a1, A_b_x
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
		la $a0, A_b_x
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 2
		addi $sp, $sp, -4
		lw $t1 0($sp)
		mul $t1, $t1, $t0
		move $t0, $t1
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
