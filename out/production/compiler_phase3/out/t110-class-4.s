.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_t :	.space	4
	Test_field : .word 0

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
		jal Test_init
		addi $sp, $sp, 0
		jal Test_get_field
		addi $sp, $sp, 0
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
	Test_init:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, Test_field
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $t0, 851236
		sw $t0, 0($a3)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	Test_get_field:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, Test_field
		lw $t0, 0($a0)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
