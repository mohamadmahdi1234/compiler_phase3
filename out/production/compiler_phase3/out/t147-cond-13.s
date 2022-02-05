.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	StringLiteral_01: .asciiz "hey there!"
	StringLiteral_11: .asciiz "true"
	StringLiteral_21: .asciiz "false"
	StringLiteral_31: .asciiz "hey man!"

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
		jal global_str
		addi $sp, $sp, 0
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		la $t0, StringLiteral_01
		addi $sp, $sp, -4
		lw $t1 0($sp)
loop1:
		lb $t3($t1)  
		lb $t4($t0)
		beqz $t3,checkt21 
		beqz $t4,missmatch1
		slt $t5,$t3,$t4  
		bnez $t5,missmatch1
		addi $t1,$t1,1  
		addi $t0,$t0,1
		j loop1
		missmatch1: 
		addi $t1,$zero,1
		j endfunction1
		checkt21:
		bnez $t4,missmatch1
		add $t1,$zero,$zero
		endfunction1:
		move $t0, $t1
		beq $t0, 0, L1
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		j L1exit
L1:
		la $t0, StringLiteral_21
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
L1exit:
		jal global_str
		addi $sp, $sp, 0
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		la $t0, StringLiteral_31
		addi $sp, $sp, -4
		lw $t1 0($sp)
loop2:
		lb $t3($t1)  
		lb $t4($t0)
		beqz $t3,checkt22 
		beqz $t4,missmatch2
		slt $t5,$t3,$t4  
		bnez $t5,missmatch2
		addi $t1,$t1,1  
		addi $t0,$t0,1
		j loop2
		missmatch2: 
		addi $t1,$zero,1
		j endfunction2
		checkt22:
		bnez $t4,missmatch2
		add $t1,$zero,$zero
		endfunction2:
		move $t0, $t1
		beq $t0, 0, L2
		la $t0, StringLiteral_11
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		j L2exit
L2:
		la $t0, StringLiteral_21
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
	global_str:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $t0, StringLiteral_01
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
