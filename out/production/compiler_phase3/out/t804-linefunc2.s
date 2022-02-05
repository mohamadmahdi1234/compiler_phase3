.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_func__line__func_line : .word 0
	global_func__line__func_func : .word 0
	global_func__line__func_input : .word 0
	global_func__line__func_out : .word 0
	func__line__func1: .asciiz "func__line__func"
	StringLiteral_01: .asciiz "Function name: "
	StringLiteral_11: .asciiz "Line number: "
	StringLiteral_21: .asciiz "Line number plus input is even!"
	StringLiteral_31: .asciiz "Line number plus input is odd!"

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
		jal global_func__line__func
		addi $sp, $sp, 0
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	global_func__line__func:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, global_func__line__func_func
		lw $t0, 0($a0)
		la $a3, 0($a0) 
	la	$t0, func__line__func1
		sw $t0, 0($a3)
		la $a0, global_func__line__func_line
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $t0, 8
		sw $t0, 0($a3)
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $a0, global_func__line__func_func
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $a0, global_func__line__func_line
		lw $t0, 0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_func__line__func_line
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 2
		addi $sp, $sp, -4
		lw $t1 0($sp)
		div $t1, $t1, $t0
		mfhi $t1
		move $t0, $t1
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		li $t0, 0
		addi $sp, $sp, -4
		lw $t1 0($sp)
		seq $t1, $t1, $t0
		move $t0, $t1
		beq $t0, 0, L1
		la $a0, global_func__line__func_out
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_21
		sw $t0, 0($a3)
		j L1exit
L1:
		la $a0, global_func__line__func_out
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		la $t0, StringLiteral_31
		sw $t0, 0($a3)
L1exit:
		la $a0, global_func__line__func_out
		lw $t0, 0($a0)
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
