.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	StringLiteral_01: .asciiz "a"
	StringLiteral_11: .asciiz "b"

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
		li $t0, 1
		beq $t0, 0, L1
		li $t0, 1
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 1
		xori $t0, $t0, 1
		addi $sp, $sp, -4
		lw $t1, 0($sp)
		or $t1, $t1, $t0
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
L2exit:
		j L1exit
L1:
L1exit:
		li $t0, 0
		beq $t0, 0, L3
		li $t0, 1
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 1
		xori $t0, $t0, 1
		addi $sp, $sp, -4
		lw $t1, 0($sp)
		or $t1, $t1, $t0
		move $t0, $t1
		beq $t0, 0, L4
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		j L4exit
L4:
L4exit:
		j L3exit
L3:
L3exit:
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
